//
//  Position.m
//  ccdream
//
//  Created by wei zhang on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Position.h"


@implementation Position

@synthesize x = _x;
@synthesize y = _y;

+(id)positionWithX:(int) x Y:(int) y{
    Position* position = [[Position alloc] init];
    position.x=x;
    position.y=y;
    [position autorelease];
    return position;
}

+(id)positionWithCGPoint:(CGPoint) point{
    Position* position = [[Position alloc] init];
    position.x=point.x/MAP_POINT_SIZE_X;
    position.y=point.y/MAP_POINT_SIZE_Y;
    [position autorelease];
    return position;
}

-(CGPoint) toLeft0Right0CGPoint{
    return ccp(self.x*MAP_POINT_SIZE_X,self.y*MAP_POINT_SIZE_Y);
}

-(CGPoint) toCenterCGPoint{
    return ccp(self.x*MAP_POINT_SIZE_X+MAP_POINT_SIZE_X/2,self.y*MAP_POINT_SIZE_Y+MAP_POINT_SIZE_Y/2);
}

-(Position*) toFightMenuPosition:(Position*) maxPosition{
    int xoffset = 2;
    int yoffset = 2;
    if(self.x>maxPosition.x*2/3){
        xoffset = -2;
    }
    if(self.y>maxPosition.y*2/3){
        yoffset = -2;
    }
    return [Position positionWithX:_x+xoffset Y:_y+yoffset];
}

-(Position*) toWeaponMenuPosition:(Position*) maxPosition{
    int xoffset = 1;
    int yoffset = 1;
    if(self.x>maxPosition.x*2/3){
        xoffset = -1;
    }
    if(self.y>maxPosition.y*2/3){
        yoffset = -1;
    }
    return [Position positionWithX:_x+xoffset Y:_y+yoffset];
}

-(NSString*) description{
    return [NSString stringWithFormat:@"x=%i,y=%i",self.x,self.y];
}
@end
