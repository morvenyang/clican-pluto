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
    position.x=point.x/MAP_POINT_SIZE;
    position.y=point.y/MAP_POINT_SIZE;
    [position autorelease];
    return position;
}

-(CGPoint) toLeft0Right0CGPoint{
    return ccp(self.x*MAP_POINT_SIZE,self.y*MAP_POINT_SIZE);
}

-(CGPoint) toCenterCGPoint{
    return ccp(self.x*MAP_POINT_SIZE+MAP_POINT_SIZE/2,self.y*MAP_POINT_SIZE+MAP_POINT_SIZE/2);
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

-(NSString*) description{
    return [NSString stringWithFormat:@"x=%i,y=%i",self.x,self.y];
}
@end
