//
//  MapGridAttribute.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Position.h"
@interface MapGridAttribute : NSObject {
    int _mapType;
    int _additionalHit;
    int _additionalAvoid;
    Position* _position;
}

@property (nonatomic,assign) int mapType;
@property (nonatomic,assign) int additionalHit;
@property (nonatomic,assign) int additionalAvoid;
@property (nonatomic,retain) Position* position;

+(id) mapGridAttribut:(NSDictionary*) properties position:(Position*) position;

@end
