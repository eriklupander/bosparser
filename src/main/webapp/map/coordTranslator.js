var coordTranslator = new function() {

	this.MAX_X = 229135;      // Height, from bottom in bos
    this.MAX_Z = 357500;      // Width, from left in bos

    var MAP_PIXEL_SIZE_X = 8192;
    var MAP_PIXEL_SIZE_Y = 5245;

	this.worldToImage = function(worldX, worldY, metaData) {
		var mapX =  ((MAX_X - worldX) / metaData.xd);
        var mapZ = worldY / metaData.zd;
        return {
            x : mapZ,
            y : mapX
        }
	}
	
	this.imageToWorld = function(imageX, imageY, metaData) {
		return {
			x : toWorldCoordX(imageY, metaData.zd),
			z : toWorldCoordY(imageX, metaData.xd)
		};
	}
	
	this.isWorldObjectVisible = function(worldX, worldY, viewport) {
		
		return worldX < viewport.tx && worldX > viewport.bx && worldY > viewport.ty && worldY < viewport.by;
	
	}
	
	this.worldToImageInViewport = function(worldX, worldY, viewport, mapWidth, mapHeight) {
		
        
        var vImageX = (((worldY - viewport.ty) / (viewport.by - viewport.ty)) * mapWidth);
        var vImageY = (1 - ((worldX - viewport.bx) / (viewport.tx - viewport.bx))) * mapHeight;
        return {
            x : vImageX,
            y : vImageY
        };
	}

    this.calculateHitBox = function(imageX, imageY, metadata, radius) {
        var box = {};
        var topLeft = coordTranslator.imageToWorld(imageX-radius, imageY-radius, metadata);
        var bottomRight = coordTranslator.imageToWorld(imageX+radius, imageY+radius, metadata);
        box.x1 = topLeft.x;
        box.y1 = topLeft.z;
        box.x2 = bottomRight.x;
        box.y2 = bottomRight.z;
        return box;
    }

    this.inHitBox = function(worldX, worldY, hitBox) {
        return worldX < hitBox.x1 && worldX > hitBox.x2 && worldY > hitBox.y1 && worldY < hitBox.y2;
    }

    this.doIfHit = function(hitbox, sData, callback) {

    }
	
	var toWorldCoordY = function(imagePixelX, xd) {
        return imagePixelX * xd;
    }
    var toWorldCoordX = function(imagePixelY, zd) {
        return coordTranslator.MAX_X - (imagePixelY*zd);
    }

};