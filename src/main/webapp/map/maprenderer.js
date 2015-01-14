var maprenderer = new function() {

    var MAP_URL = "";
    //var MAX_X = 231000;      // Height, from bottom in bos
    //var MAX_Z = 357500;      // Width, from left in bos

    this.mapHeight;
    this.mapWidth;

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

            modelhelper.enrichKillsWithHits(sData);

            var width = $(window).width() * 0.9;
            var height = $(window).height() * 0.80;

            $("#map").attr('width', width);
            $("#map").attr('height', height);

            maprenderer.mapHeight = $('#map').attr('height');
            maprenderer.mapWidth = $('#map').attr('width');

            $("#map").mousedown(function (e) {
                startX=parseInt(e.clientX-0);
                startY=parseInt(e.clientY-0);
                var canvasOffset=$("#map").offset();

                startXX=parseInt(e.clientX-canvasOffset.left);
                startYY=parseInt(e.clientY-canvasOffset.top);
                mouseDown = true;
                // Test select
                var metadata = {"xd":xd, "zd":zd};
                var worldCoords = coordTranslator.imageToWorld(imageX+startXX*zoom, imageY+startYY*zoom, metadata);


                var hitBox = coordTranslator.calculateHitBox(imageX+startXX*zoom, imageY+startYY*zoom, metadata, 16);
                var hit = false;
                // Now what, check every clickable object? Try kills!
                for(var a = 0; a < sData.kills.length; a++) {

                    console.log("Hitbox: " + hitBox.x1 + "," + hitBox.y1 + " -> " + hitBox.x2 + "," + hitBox.y2);
                    if(sData.kills[a].parentId == -1 && coordTranslator.inHitBox(sData.kills[a].killedXPos, sData.kills[a].killedZPos, hitBox)) {
                        hit = true;
                        renderer.renderKillInfoDialog(sData.kills[a]);
                        break;
                    }
                }
                if(!hit) {
                    $('#gid-dialog').addClass("hidden");
                }
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
                zoom = zoom - (wheel/15)*zoom;

                var currentWidth = maprenderer.mapWidth*lastZoom;
                var currentHeight = maprenderer.mapHeight*lastZoom;
                var newWidth = maprenderer.mapWidth*zoom;
                var newHeight = maprenderer.mapHeight*zoom;

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
            x1 : coordTranslator.MAX_X, z1 : coordTranslator.MAX_Z
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

        xd = coordTranslator.MAX_X / MAP_PIXEL_SIZE_Y;
        zd = coordTranslator.MAX_Z / MAP_PIXEL_SIZE_X;

        var startPos = translateToPixel(lowerBounds.x1, lowerBounds.z1);
        imageX = startPos.x - maprenderer.mapWidth/2;
        imageY = startPos.y - maprenderer.mapHeight/2;

        var context = buffer.getContext('2d');
        var imageObj = new Image();

        imageObj.onload = function() {
            $body.removeClass("loading");
            // Render everything AFTER image has loaded, otherwise map renders over other stuff
            context.drawImage(imageObj, 0, 0, MAP_PIXEL_SIZE_X, MAP_PIXEL_SIZE_Y);
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

        ctx.drawImage(buffer, imageX, imageY, maprenderer.mapWidth*zoom, maprenderer.mapHeight*zoom, 0, 0, maprenderer.mapWidth, maprenderer.mapHeight);
        var viewport = calcViewPortWorldCoordinates();
        renderer.renderFlightMapVectorized(viewport, ctx, sData);
        renderer.renderKillsOnMapVectorized(viewport, ctx, sData);
    }

    var toWorldCoordY = function(pixel) {
        return pixel * xd;
    }
    var toWorldCoordX = function(pixel) {
        return coordTranslator.MAX_X - (pixel*zd);
    }

//    var getImageCoordsInCurrentViewport = function(viewport, objX, objY) {
//        //if(objX < viewport.tx && objX > viewport.bx && objY > viewport.ty && objY < viewport.by) {
//            // If on screen, translate into current pixel coords and render...
//        var vImageX = (((objY - viewport.ty) / (viewport.by - viewport.ty)) * this.mapWidth);
//        var vImageY = (1 - ((objX - viewport.bx) / (viewport.tx - viewport.bx))) * this.mapHeight;
//        return {
//            x : vImageX,
//            y : vImageY
//        };
//    }

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
            by : toWorldCoordY(imageX+maprenderer.mapWidth*zoom),
            bx : toWorldCoordX(imageY+maprenderer.mapHeight*zoom)
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
         //   console.log("img x:" + imageX + ", img y:" + imageY);

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

                var currentWidth = maprenderer.mapWidth*lastZoom;
                var currentHeight = maprenderer.mapHeight*lastZoom;
                var newWidth = maprenderer.mapWidth*zoom;
                var newHeight = maprenderer.mapHeight*zoom;

                var moveImageXBy = (currentWidth-newWidth) * 0.5;
                var moveImageYBy = (currentHeight-newHeight) * 0.5;
                imageX += moveImageXBy;
                imageY += moveImageYBy;

                draw();
            }
            startY = mouseY;

        }
    }

//    var renderKillsOnMap = function(context, data) {
//        context.save();
//        if(data.kills.length > 0) {
//            for(var a = 0; a < data.kills.length; a++) {
//                var kill = data.kills[a];
//                if(kill.parentId == -1 && kill.killedXPos != null && kill.killedZPos != null)   {
//                    var coord = translateToPixel(kill.killedXPos, kill.killedZPos);
//                    drawKill(coord.x, coord.y, 10, kill.type, context);
//                }
//            }
//        }
//        context.restore();
//    }





    /**
     * Map a BOS x,z coordinate to a pixel coord on our map
     *
     * @param x
     * @param z
     */
    var translateToPixel= function(x, z) {
        var mapX =  ((coordTranslator.MAX_X - x) / xd);
        var mapZ = z / zd;
        return {
            x : mapZ,
            y : mapX
        }
    };

}