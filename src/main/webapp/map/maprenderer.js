var maprenderer = new function() {

    var MAP_URL = "";
    var MAX_X = 500000;      // Height, from bottom in bos
    var MAX_Z = 500000;      // Width, from left in bos

    var mapHeight, mapWidth;

    this.renderMission = function(missionid) {
        $('#clickme').addClass("hidden");
        $('#missions-inner').addClass('hidden');
        if(missionid == null) return;

        $.get('/rest/view/reports/' + missionid, function(data) {

            $('#map').removeClass('hidden');
            mapHeight = $('#map').height;
            mapWidth = $('#map').width;

            drawMapImage();

            // Plot each kill on the map.
            if(data.kills.length > 0) {
                for(var a = 0; a < data.kills.length; a++) {
                    var kill = data.kills[a];
                    var coord = translateToPixel(kill.killedXPos, kill.killedZPos);
                    drawKill(coord.x, coord.y, 40, kill.type);
                }
            }


        });

    }

    var drawKill = function(x, y, size, label) {
        var canvas = document.getElementById('map');
        var context = canvas.getContext('2d');
        context.beginPath();
        context.arc(x, y, size, 0, 2 * Math.PI, false);
        context.fillStyle = 'green';
        context.fill();
        context.lineWidth = 5;
        context.strokeStyle = '#003300';
        context.stroke();

        context.font = '16pt arial';
        context.textAlign = 'center';

        context.fillText(label, x, y+size+20);
    }

    var drawMapImage = function() {
        var canvas = document.getElementById('map');
        var context = canvas.getContext('2d');
        var imageObj = new Image();

        imageObj.onload = function() {
            context.drawImage(imageObj, 0, 0);
        };
        imageObj.src = '../images/map.jpg';
    }

    /**
     * Map a BOS x,z coordinate to a pixel coord on our map
     *
     * @param x
     * @param z
     */
    var translateToPixel= function(x, z) {
        var mapX = mapHeight - (x / mapHeight);
        var mapZ = z / mapWidth;
        return {
            x : mapX,
            y : mapY
        }
    }

}