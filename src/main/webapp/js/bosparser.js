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
                    tpl +=  '<div class="col-md-3">' + kill.type + '</div>';
                    tpl +=  '<div class="col-md-3">' + kill.gameTime + '</div>';
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
                    tpl +=  '<div class="col-md-2">' + hit.target + '</div>';
                    tpl +=  '<div class="col-md-1">' + hit.targetId + '</div>';
                    tpl +=  '<div class="col-md-2">' + hit.gameTime + '</div>';
                    tpl +=  '<div class="col-md-5">' + hit.ammo + '</div>';
                    tpl +=  '<div class="col-md-2">' + (hit.damage != null ? hit.damage : '') + '</div>';
                    tpl +=  '</div>';
                    $('#hits-table').append(tpl);
                }
            } else {
                $('#hits-table').find(".itemrow").remove();
                $('#hits-table').append('<div class="row itemrow"><div class="col-md-12">No hits recorded on this mission</div></div>');
            }
        });
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