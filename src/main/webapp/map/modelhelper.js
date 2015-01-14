var modelhelper = new function() {

    this.enrichKillsWithHits = function(sData) {

        for(var a = 0; a < sData.kills.length; a++) {
            var kill = sData.kills[a];
            var hits = {};
            var totalHits = 0;
            for(var b = 0; b < sData.hits.length; b++) {
                var hit = sData.hits[b];
                if(hit.targetId == kill.gameObjectId) {
                    var ammo = hit.ammo;
                    if(typeof hits[ammo] == 'undefined') {
                        hits[ammo] = 1;
                    } else {
                        hits[ammo] = hits[ammo] + 1;
                    }
                    totalHits++;
                }
            }
            kill.hits = hits;
            kill.totalHits = totalHits;
        }

    }

}