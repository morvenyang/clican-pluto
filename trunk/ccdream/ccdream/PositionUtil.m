//
//  PositionUtil.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 HP. All rights reserved.
//

#import "PositionUtil.h"


@implementation PositionUtil

+(CCArray*) calcPosiArray: (Position*) charPosi 
{
    CCArray* array = [CCArray arrayWithCapacity:13];
    int xStart = charPosi.x-2;
    int xEnd = charPosi.x+2;
    int yStart = charPosi.y-2;
    int yEnd = charPosi.y+2;
    
    int charMobility = 2;
    
    for(int x=xStart;x<=xEnd;x++){
        for(int y=yStart;y<=yEnd;y++){
            int xOffset =  fabs(charPosi.x - x);
            int yOffset = fabs(charPosi.y - y);
            if(xOffset+yOffset<=charMobility){
                [array addObject:[Position initWithX:x Y:y]];
            }
        }
    }
    
    return array;
}

+(CGPoint) locationFromTouch:(UITouch*)touch
{
	CGPoint touchLocation = [touch locationInView: [touch view]];
	return [[CCDirector sharedDirector] convertToGL:touchLocation];
}

+(bool) isTouch:(UITouch*)touch forNode:(CCNode*) node
{
    CGPoint touchLocation = [self locationFromTouch:touch];
    return CGRectContainsPoint([node boundingBox], touchLocation);
}
@end
