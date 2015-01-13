var renderer = new function() {

    this.renderKillsOnMapVectorized = function(viewport, context, data) {

        context.save();
        if(data.kills.length > 0) {
            for(var a = 0; a < data.kills.length; a++) {
                var kill = data.kills[a];
                if(kill.parentId == -1 && kill.killedXPos != null && kill.killedZPos != null)   {
                    var coord = coordTranslator.worldToImageInViewport(kill.killedXPos, kill.killedZPos, viewport, maprenderer.mapWidth, maprenderer.mapHeight);
                    drawKill(coord.x, coord.y, 10, kill.type, context);
                }
            }
        }
        context.restore();
    }

    this.renderFlightMapVectorized = function(viewport, context, data) {

        context.save();
        if(data.flightTrack.length > 1) {
            var startingCoord = coordTranslator.worldToImageInViewport(data.flightTrack[0].x, data.flightTrack[0].z, viewport, maprenderer.mapWidth, maprenderer.mapHeight);

            context.beginPath();

            context.moveTo(startingCoord.x, startingCoord.y);
            for(var a = 1; a < data.flightTrack.length; a++) {
                var fp = data.flightTrack[a];
                drawFlightPathVectorized(viewport, fp, context);
            }
            context.stroke();
        }
        context.moveTo(startingCoord.x, startingCoord.y);
        drawStartSymbol(startingCoord.x, startingCoord.y, context);
        var secondEndingCoord = coordTranslator.worldToImageInViewport(data.flightTrack[data.flightTrack.length - 2].x, data.flightTrack[data.flightTrack.length - 2].z, viewport, maprenderer.mapWidth, maprenderer.mapHeight);
        var endingCoord = coordTranslator.worldToImageInViewport(data.flightTrack[data.flightTrack.length - 1].x, data.flightTrack[data.flightTrack.length - 1].z, viewport, maprenderer.mapWidth, maprenderer.mapHeight);
        drawEndSymbol(endingCoord.x, endingCoord.y, secondEndingCoord.x, secondEndingCoord.y, context);
        context.restore();
    }


     var drawFlightPathVectorized = function(viewport, fp, context) {
        var coord = coordTranslator.worldToImageInViewport(fp.x, fp.z, viewport, maprenderer.mapWidth, maprenderer.mapHeight);
        context.lineWidth = 2;
        context.lineTo(coord.x, coord.y);
        context.arc(coord.x, coord.y, 2, 0, 2*Math.PI, false);
        context.moveTo(coord.x, coord.y);
    }

    var drawKill = function(x, y, size, label, context) {

        context.beginPath();
        context.arc(x, y, size, 0, 2 * Math.PI, false);
        context.fillStyle = 'red';
        context.globalAlpha = 0.8;
        context.fill();
        context.globalAlpha = 1.0;
        context.lineWidth = 2;
        context.strokeStyle = '#550000';
        context.stroke();

        context.font = '12pt Open Sans';
        context.textAlign = 'center';

        context.fillText(label, x, y+size+16);

        context.font = '10pt Open Sans';
        context.textAlign = 'center';

        context.fillStyle = 'white';
        context.fillText("K", x, y+4);

    }

    var drawStartSymbol = function(x, y, context) {
        context.save();

        context.beginPath();
        context.arc(x, y, 15, 0, Math.PI*2, false);
        context.closePath();
        context.lineWidth = 3;
        context.fillStyle = 'green';
        context.globalAlpha = 0.8;
        context.fill();
        context.globalAlpha = 1.0;
        context.strokeStyle = '#005500';
        context.stroke();

        context.font = '12pt arial';
        context.textAlign = 'center';

        context.fillStyle = 'white';
        context.fillText("S", x, y+5);

        context.restore();
    }

    var drawEndSymbol = function(x, y, x2, y2, context) {
        context.save();
        context.beginPath();
        context.moveTo(x2, y2);
        context.lineWidth = 2;
        context.lineTo(x, y);
        context.closePath();
        context.stroke();

        context.beginPath();
        context.arc(x, y, 15, 0, Math.PI*2, false);
        context.closePath();
        context.lineWidth = 3;
        context.fillStyle = 'red';
        context.globalAlpha = 0.8;
        context.fill();
        context.globalAlpha = 1.0;
        context.strokeStyle = '#550000';
        context.stroke();

        context.font = '12pt arial';
        context.textAlign = 'center';

        context.fillStyle = 'white';
        context.fillText("E", x, y+5);
        context.restore();
    }
}