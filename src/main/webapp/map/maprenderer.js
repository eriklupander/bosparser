var maprenderer = new function() {

    var MAP_URL = "";
    var MAX_X = 230000;      // Height, from bottom in bos
    var MAX_Z = 357500;      // Width, from left in bos

    var mapHeight, mapWidth;
    var zoom = 1.0;

    var MAP_PIXEL_SIZE_X = 8192;
    var MAP_PIXEL_SIZE_Y = 5245;

    var mouseX, mouseY, imageX = 0, imageY = 0, startX = 0, startY = 0, offsetX = 0, offsetY = 0;


    var xd, zd; // factor between map res and map coords

    var buffer;

    var mouseDown = false;

    var sData;

    this.renderMission = function(missionid) {
        mouseX = 0, mouseY = 0, imageX = 0, imageY = 0, startX = 0, startY = 0, offsetX = 0, offsetY = 0, zoom = 1.0;
       // $('#mapcontainer').removeClass('hidden');

        // Disable browser context menu on canvas.
        $('#map').contextmenu( function() {
            return false;
        });

        if(missionid == null) return;
        $body.addClass("loading");
        $.get('/rest/view/reports/' + missionid, function(data) {
            sData = data;
            var width = $(window).width() * 0.9;
            var height = $(window).height() * 0.80;

            $("#map").attr('width', width);
            $("#map").attr('height', height);

            mapHeight = $('#map').attr('height');
            mapWidth = $('#map').attr('width');
            $("#map").mousedown(function (e) {
                startX=parseInt(e.clientX-offsetX);
                startY=parseInt(e.clientY-offsetY);
                mouseDown = true;
            });
            $("#map").mouseout(function (e) {
                mouseDown = false;
            });
            $("#map").mouseover(function (e) {
                mouseDown = false;
            });
            $("#map").mousemove(function (e) {
                onDrag(e);
            });
            $("#map").mouseup(function (e) {
                mouseDown = false;
            });

            $("#map").bind('mousewheel', function(e){
                if(typeof e.originalEvent.wheelDelta == 'undefined' || e.originalEvent.wheelDelta == null) {
                    return;
                }
                var wheel = e.originalEvent.wheelDelta/120;
                var lastZoom = zoom;
                zoom = zoom - (wheel/30)*zoom;

                var currentWidth = mapWidth*lastZoom;
                var currentHeight = mapHeight*lastZoom;
                var newWidth = mapWidth*zoom;
                var newHeight = mapHeight*zoom;

                var moveImageXBy = (currentWidth-newWidth) * 0.5;
                var moveImageYBy = (currentHeight-newHeight) * 0.5;
                imageX += moveImageXBy;
                imageY += moveImageYBy;

                e.preventDefault();
                draw();
            });

            renderToCanvas(MAP_PIXEL_SIZE_X, MAP_PIXEL_SIZE_Y, data);
        });

    }

    var renderToCanvas = function (width, height, data) {
        buffer = document.createElement('canvas');
        buffer.width = width;
        buffer.height = height;
        renderFunction(data); //buffer.getContext('2d'));
    };

    var renderFunction = function(data) {
        zoom = 1.0;
        // We need to find the min/max x and z coords for the mission
        var lowerBounds = {
            x1 : MAX_X, z1 : MAX_Z
        }

        var upperBounds = {
            x1 : 0, z1 : 0
        }

        if(data.flightTrack.length > 1) {
            for(var a = 0; a < data.flightTrack.length; a++) {
                var fp = data.flightTrack[a];
                if(fp.x > upperBounds.x1) {upperBounds.x1 = fp.x;}
                if(fp.z > upperBounds.z1) {upperBounds.z1 = fp.z;}
                if(fp.x < lowerBounds.x1) {lowerBounds.x1 = fp.x;}
                if(fp.z < lowerBounds.z1) {lowerBounds.z1 = fp.z;}
            }
        }

        xd = MAX_X / MAP_PIXEL_SIZE_Y;
        zd = MAX_Z / MAP_PIXEL_SIZE_X;

        var startPos = translateToPixel(lowerBounds.x1, lowerBounds.z1);
        imageX = startPos.x - mapWidth/2;
        imageY = startPos.y - mapHeight/2;

        var context = buffer.getContext('2d');
        var imageObj = new Image();

        imageObj.onload = function() {
            $body.removeClass("loading");
            // Render everything AFTER image has loaded, otherwise map renders over other stuff
            context.drawImage(imageObj, 0, 0, MAP_PIXEL_SIZE_X, MAP_PIXEL_SIZE_Y);

            // Draw the flight track on the map
            renderFlightMap(context, data);

            // Plot each kill on the map.
            //renderKillsOnMap(context, data);

            draw();

            $('#map').css('background-color', 'rgba(158, 167, 184, 1.0)');
            $('#map').removeClass('hidden');
        };
        imageObj.src = '../images/Stalingrad_map.jpg';
    }





    var draw = function() {
        var canvas = document.getElementById('map');
        var ctx = canvas.getContext('2d');

        // TODO - the clearrect should only be necessary if imageX or imageY causes
        // render outside of map
        ctx.clearRect ( 0 , 0 , canvas.width, canvas.height );
        if(imageX < 0) imageX = 0;
        if(imageY < 0) imageY = 0;

        ctx.drawImage(buffer, imageX, imageY, mapWidth*zoom, mapHeight*zoom, 0, 0, mapWidth, mapHeight);
        renderKillsOnMapVectorized(ctx, sData);
        // Test, draw something "vectorized". Same size regardless of zoom but in correct spot.

        // Translate the top left and bottom right coords back into "BoS" coords
//        var ty = toWorldCoordY(imageX);
//        var tx = toWorldCoordX(imageY);
//
//        var by = toWorldCoordY(imageX+mapWidth*zoom)
//        var bx = toWorldCoordX(imageY+mapHeight*zoom);
//
//        // Check if our object is visible
//        var objX = 96000;
//        var objY = 56000;
//
//        if(objX < tx && objX > bx && objY > ty && objY < by) {
//            // If on screen, translate into current pixel coords and render...
//            var vImageX = (((objY - ty) / (by - ty)) * mapWidth);
//            var vImageY = (1 - ((objX - bx) / (tx - bx))) * mapHeight;
//
//            var radius = 35;
//            ctx.beginPath();
//            ctx.arc(vImageX, vImageY, radius, 0, 2 * Math.PI, false);
//            ctx.fillStyle = 'green';
//            ctx.fill();
//            ctx.lineWidth = 5;
//            ctx.strokeStyle = '#003300';
//            ctx.stroke();
//        }



    }

    /*
     var mapX =  ((MAX_X - x) / xd);
     var mapZ = z / zd;
     return {
     x : mapZ,
     y : mapX
     }
     */
    var toWorldCoordY = function(pixel) {
        return pixel * xd;
    }
    var toWorldCoordX = function(pixel) {
        return MAX_X - (pixel*zd);
    }

    var getImageCoordsInCurrentViewport = function(viewport, objX, objY) {
        //if(objX < viewport.tx && objX > viewport.bx && objY > viewport.ty && objY < viewport.by) {
            // If on screen, translate into current pixel coords and render...
        var vImageX = (((objY - viewport.ty) / (viewport.by - viewport.ty)) * mapWidth);
        var vImageY = (1 - ((objX - viewport.bx) / (viewport.tx - viewport.bx))) * mapHeight;
        return {
            x : vImageX,
            y : vImageY
        };
    }

    /**
     * Returns the current viewport map as BoS world coordinates
     *
     * Remember that BoS uses X as vertical axis starting from bottom, Y as horizontal starting from left.
     *
     * @returns {{ty: *, tx: *, by: *, bx: *}}
     */
    var calcViewPortWorldCoordinates = function() {
        var viewport = {
            ty : toWorldCoordY(imageX),
            tx : toWorldCoordX(imageY),
            by : toWorldCoordY(imageX+mapWidth*zoom),
            bx : toWorldCoordX(imageY+mapHeight*zoom)
        };
        return viewport;
    }

    var onDrag = function(e) {
        if(!mouseDown) return;

        // DRAG
        if( e.which == 1 && !(e.which == 1 && e.buttons == 2)) {
            mouseX = parseInt(e.clientX - offsetX);
            mouseY = parseInt(e.clientY - offsetY);

            // move the image by the amount of the latest drag
            var dx = mouseX - startX;
            var dy = mouseY - startY;

            imageX = imageX ? imageX + -dx*2 : -dx*2;
            imageY = imageY ? imageY + -dy*2 : -dy*2;
            if(imageX < 0) imageX = 0;
            if(imageY < 0) imageY = 0;

           // if(imageX > 8192-1500*zoom) imageX = 8192-mapWidth;
          //  if(imageY > 5245-1100*zoom) imageY = 5245-mapHeight;
            console.log("img x:" + imageX + ", img y:" + imageY);

            startX = mouseX;
            startY = mouseY;

            draw();
        }

        // ZOOM
        if( e.which == 3 || (e.which == 1 && e.buttons == 2)) {
            mouseY = parseInt(e.clientY - offsetY);
            var dy = mouseY - startY;
            var lastZoom = zoom;
            zoom = zoom ? zoom + dy/ (500/zoom) : dy/ (500/zoom);

            if(zoom > 3.1) {
                zoom = 3.1;
            }else  if(zoom < 0.1) {
                zoom = 0.1;
            } else {
                // Recalculate imageX and imageY so zooming is centered.

                // Get width/height before and then after next zoom.
                // Take each difference and split in half. Add/subtract to x and y.

                var currentWidth = mapWidth*lastZoom;
                var currentHeight = mapHeight*lastZoom;
                var newWidth = mapWidth*zoom;
                var newHeight = mapHeight*zoom;

                var moveImageXBy = (currentWidth-newWidth) * 0.5;
                var moveImageYBy = (currentHeight-newHeight) * 0.5;
                imageX += moveImageXBy;
                imageY += moveImageYBy;

                 /*
                  self.scrollView.zoomScale = 1;
                  self.scrollView.minimumZoomScale = fminf(1, fminf(frame.size.width/(image.size.width+1), frame.size.height/(image.size.height+1)));
                  CGFloat leftMargin = (frame.size.width - image.size.width)*0.5;
                  CGFloat topMargin = (frame.size.height - image.size.height)*0.5;
                  self.imageView.frame = CGRectMake(0, 0, image.size.width, image.size.height);
                  [self assignInsetsOnScroller]; // this has to be done before settings contentOffset!
                  self.scrollView.contentOffset = CGPointMake(fmaxf(0,-leftMargin), fmaxf(0,-topMargin));
                  self.scrollView.contentSize = CGSizeMake(fmaxf(image.size.width, frame.size.width+1), fmaxf(image.size.height, frame.size.height+1));
                 
                 
                 
                 
                 
                 */
                // We should do something here to make the zoom occur from center,
                // not top-left
               // imageX = imageX + (imageX*zoom);
               // imageY = imageY + (imageY*zoom);
//                if(dy < 0) {
//                    imageX = imageX + 1;
//                    imageY = imageY + 1;
//                } else {
//                    imageX = imageX - 1;
//                    imageY = imageY - 1;
//                }

                draw();
            }
            startY = mouseY;

        }
    }

    var renderKillsOnMap = function(context, data) {
        context.save();
        if(data.kills.length > 0) {
            for(var a = 0; a < data.kills.length; a++) {
                var kill = data.kills[a];
                if(kill.parentId == -1 && kill.killedXPos != null && kill.killedZPos != null)   {
                    var coord = translateToPixel(kill.killedXPos, kill.killedZPos);
                    drawKill(coord.x, coord.y, 10, kill.type, context);
                }
            }
        }
        context.restore();
    }

    var renderKillsOnMapVectorized = function(context, data) {
        var viewport = calcViewPortWorldCoordinates();
        context.save();
        if(data.kills.length > 0) {
            for(var a = 0; a < data.kills.length; a++) {
                var kill = data.kills[a];
                if(kill.parentId == -1 && kill.killedXPos != null && kill.killedZPos != null)   {
                    var coord = getImageCoordsInCurrentViewport(viewport, kill.killedXPos, kill.killedZPos);
                    drawKill(coord.x, coord.y, 10, kill.type, context);
                }
            }
        }
        context.restore();
    }

    var renderFlightMap = function(context, data) {
        context.save();
        if(data.flightTrack.length > 1) {
            var startingCoord = translateToPixel(data.flightTrack[0].x, data.flightTrack[0].z);

            context.beginPath();
            context.moveTo(startingCoord.x, startingCoord.y);
            drawStartSymbol(startingCoord.x, startingCoord.y, context);
            context.moveTo(startingCoord.x, startingCoord.y);
            for(var a = 1; a < data.flightTrack.length; a++) {
                var fp = data.flightTrack[a];
                drawFlightPath(fp, context);
            }
            context.stroke();
        }
        var secondEndingCoord = translateToPixel(data.flightTrack[data.flightTrack.length - 2].x, data.flightTrack[data.flightTrack.length - 2].z);
        var endingCoord = translateToPixel(data.flightTrack[data.flightTrack.length - 1].x, data.flightTrack[data.flightTrack.length - 1].z);
        drawEndSymbol(endingCoord.x, endingCoord.y, secondEndingCoord.x, secondEndingCoord.y, context);
        context.restore();
    }

    var drawFlightPath = function(fp, context) {
        var coord = translateToPixel(fp.x, fp.z);
        context.lineWidth = 2;
        context.lineTo(coord.x, coord.y);

       // context.rotate(230);
        context.arc(coord.x, coord.y, 2, 0, 2*Math.PI, false);
        context.moveTo(coord.x, coord.y);
       // context.rotate(-230);
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

    /**
     * Map a BOS x,z coordinate to a pixel coord on our map
     *
     * @param x
     * @param z
     */
    var translateToPixel= function(x, z) {
        var mapX =  ((MAX_X - x) / xd);
        var mapZ = z / zd;
        return {
            x : mapZ,
            y : mapX
        }
    };

}