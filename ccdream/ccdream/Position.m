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

-(NSString*) description{
    return [NSString stringWithFormat:@"x=%i,y=%i",self.x,self.y];
}
@end
