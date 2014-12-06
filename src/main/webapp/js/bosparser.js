var bosparser = new function() {


    this.scan = function() {
        $.ajax({
            'method' : 'POST',
            'url' : '/rest/view/reports',
            'complete' : function(data) {
                $('#modal-content').html(data.responseText);
                $('#myModal').modal({show:true});
                bosparser.populateSidebar();
            }
        });
    }

    this.deleteAll = function() {
        $.ajax({
            'method' : 'DELETE',
            'url' : '/rest/view/reports',
            'complete' : function(data) {
                $('#modal-content').html(data.responseText);
                $('#myModal').modal({show:true});
                bosparser.populateSidebar();
            }
        });
    }

    this.populateSidebar = function() {
        $.get('/rest/view/tinyreports', function(data) {
            $('#sidebar').empty();
            for(var a = 0; a < data.length; a++) {
                var tpl = '<li id="mission_'+data[a].id + '"><a href="#">'+data[a].title + '<div style="font-size:8pt;">'+data[a].created + '</div></a></li>';
                $('#sidebar').append(tpl);
                $('#mission_' + data[a].id).click(
                    function(_id) {
                       return function() {
                           bosparser.populateMission(_id);
                       }
                    }(data[a].id)

                );
            }
        });
    }            //<li><a href="#sec1">Section 1</a></li>

    this.populateMission = function(missionid) {
        if(missionid == null) return;
        if(typeof console.log != 'undefined' && console.log !=  null) {
            console.log("Loading mission " + missionid);
        }

        $.get('/rest/view/reports/' + missionid, function(data) {

            $('#mission-name').html(data.missionName);
            $('#mission-date').html(data.gameDate);
            $('#mission-time').html(data.gameTime);
            $('#mission-duration').html(data.totalDuration);

             $('#pilot-name').html(data.pilotName);
             $('#pilot-plane').html(data.pilotPlane);
             $('#pilot-status').html('FIXME');

            $('#ac-kills').html(data.aircraftKillCount);
            $('#ac-damaged').html(data.aircraftHitNotKilledCount);
            $('#crew-kills').html(data.pilotKillCount);

             $('#bullets-fired').html(data.startingAmmo - data.finalAmmo);
             $('#bullets-hit').html(data.hits.length);
             $('#bullets-left').html(data.finalAmmo != null ? data.finalAmmo : 'Crashed');


            // Kills table
            /*
             <div class="row">
             <div class="col-md-3">Pe-2</div>
             <div class="col-md-3">00:14:34</div>
             <div class="col-md-2">5</div>
             <div class="col-md-2">Alive</div>
             </div>
             */
            if(data.kills.length > 0) {
                $('#kills-table').find(".itemrow").remove();
                for(var a = 0; a < data.kills.length; a++) {
                    var kill = data.kills[a];
                     var tpl = '<div class="row itemrow">';
                    tpl +=  '<div class="col-md-2">' + kill.name + '</div>';
                    tpl +=  '<div class="col-md-3">' + kill.type + '</div>';
                    tpl +=  '<div class="col-md-1">' + kill.gameObjectId + '</div>';
                    tpl +=  '<div class="col-md-2">' + kill.gameTime + '</div>';
                    tpl +=  '<div class="col-md-2">' + countHits(data.hits, kill.gameObjectId) + '</div>';
                    tpl +=  '<div class="col-md-2">' + 'FIXME' + '</div>';
                    tpl +=  '</div>';
                    $('#kills-table').append(tpl);
                }
            } else {
                $('#kills-table').find(".itemrow").remove();
                $('#kills-table').append('<div class="row itemrow"><div class="col-md-12">No kills recorded on this mission</div></div>');
            }



            // Hits
            /*
             <div class="row">
             <div class="col-md-3">Pe-2</div>
             <div class="col-md-2">00:14:34</div>
             <div class="col-md-5">7.92mm AP</div>
             <div class="col-md-2">0.05</div>
             </div>

             */
            if(data.hits.length > 0) {
                $('#hits-table').find(".itemrow").remove();
                for(var a = 0; a < data.hits.length; a++) {
                    var hit = data.hits[a];
                    var tpl = '<div class="row itemrow">';
                    tpl +=  '<div class="col-md-2">' + hit.name + '</div>';
                    tpl +=  '<div class="col-md-3">' + hit.target + '</div>';
                    tpl +=  '<div class="col-md-1">' + hit.targetId + '</div>';
                    tpl +=  '<div class="col-md-1">' + hit.gameTime + '</div>';
                    tpl +=  '<div class="col-md-3">' + hit.ammo + '</div>';
                    tpl +=  '<div class="col-md-2">' + (hit.damage != null ? hit.damage : '') + '</div>';
                    tpl +=  '</div>';
                    $('#hits-table').append(tpl);
                }
            } else {
                $('#hits-table').find(".itemrow").remove();
                $('#hits-table').append('<div class="row itemrow"><div class="col-md-12">No hits recorded on this mission</div></div>');
            }

            if(data.hitsTaken.length > 0) {
                $('#hitstaken-table').find(".itemrow").remove();
                for(var a = 0; a < data.hitsTaken.length; a++) {
                    var hit = data.hitsTaken[a];
                    var tpl = '<div class="row itemrow">';
                    tpl +=  '<div class="col-md-2">' + hit.attackerName + '</div>';
                    tpl +=  '<div class="col-md-3">' + hit.attacker + '</div>';
                    tpl +=  '<div class="col-md-1">' + hit.attackerId + '</div>';
                    tpl +=  '<div class="col-md-1">' + hit.gameTime + '</div>';
                    tpl +=  '<div class="col-md-3">' + hit.ammo + '</div>';
                    tpl +=  '<div class="col-md-2">' + (hit.damage != null ? hit.damage : '') + '</div>';
                    tpl +=  '</div>';
                    $('#hitstaken-table').append(tpl);
                }
            } else {
                $('#hitstaken-table').find(".itemrow").remove();
                $('#hitstaken-table').append('<div class="row itemrow"><div class="col-md-12">No hits taken on this mission</div></div>');
            }


            if(data.gameObjectHierarchy.length > 0) {
                var tpl = '';
                for(var a = 0; a < data.gameObjectHierarchy.length; a++) {
                    var obj = data.gameObjectHierarchy[a];
                    tpl += buildTree(obj);
                }
                $('#objtree').empty().html(tpl);


                    $('.tree li:has(ul)').addClass('parent_li').find(' > span').attr('title', 'Collapse this branch');

                    $('.tree li.parent_li > span').on('click', function (e) {
                        var children = $(this).parent('li.parent_li').find(' > ul > li');
                        if (children.is(":visible")) {
                            children.hide('fast');
                            $(this).attr('title', 'Expand').find(' > i').addClass('glyphicon-plus-sign').removeClass('glyphicon-minus-sign');
                        } else {
                            children.show('fast');
                            $(this).attr('title', 'Collapse').find(' > i').addClass('glyphicon-minus-sign').removeClass('glyphicon-plus-sign');
                        }
                        e.stopPropagation();
                    });


            }
        });
    }

    var buildTree = function(obj) {
        var tpl = '<ul><li> <span><i class="glyphicon glyphicon-folder-open"></i> '+obj.type+'</span>';
        if(obj.children.length == 0) {
            tpl += '</li></ul>';
            return tpl;
        } else {
            for(var a = 0; a < obj.children.length; a++) {
                tpl += buildTree(obj.children[a]);
            }
        }
        tpl += '</li></ul>';
        return tpl;
    }

    var countHits = function(hits, gameObjectId) {
        var cnt = 0;
        for(var a = 0; a < hits.length; a++) {
            if(hits[a].targetId == gameObjectId) {
                cnt++;
            }
        }
        return cnt;
    }

};