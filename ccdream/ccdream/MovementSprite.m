//
//  MovementSprite.m
//  ccdream
//
//  Created by wei zhang on 12-6-3.
//  Copyright 2012年 HP. All rights reserved.
//

#import "MovementSprite.h"


@implementation MovementSprite


+(id) initWithPosiArray:(CCArray*) posiArray {

    MovementSprite* movementSprite = [MovementSprite node];

    for(int i=0;i<[posiArray count];i++){
        MoveOrbit* mo = [posiArray objectAtIndex:i];
        CCLOG(@"%@",[mo.position description]);
        CCSprite* redSprite = [[CCSprite alloc] init];
        [redSprite setColor:ccc3(255, 0, 0)];
        redSprite.opacity=100;
        redSprite.textureRect = CGRectMake(0, 0, MAP_POINT_SIZE, MAP_POINT_SIZE);
        
        redSprite.position = CGPointMake(mo.position.x*32+16, mo.position.y*32+16);
        [movementSprite addChild:redSprite];
    }
    
    return movementSprite;
}
@end
