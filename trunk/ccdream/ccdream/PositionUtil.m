//
//  PositionUtil.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 HP. All rights reserved.
//

#import "PositionUtil.h"


@implementation PositionUtil


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
