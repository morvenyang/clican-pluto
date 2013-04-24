//
//  MoveOrbit.h
//  ccdream
//
//  Created by wei zhang on 12-6-4.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Position.h"

@interface MoveOrbit : NSObject {
    MoveOrbit* _previous;
    Position* _position;
    int _moveDistance;
    int _spentMovement;//消耗的移动力
}

@property (nonatomic,retain) MoveOrbit* previous;
@property (nonatomic,retain) Position* position;
@property (nonatomic,assign) int moveDistance;
@property (nonatomic,assign) int spentMovement;

+(id)moveOrbitWithPrevious:(MoveOrbit*) previous Position:(Position*) position;

+(id)posiComparator;

+(id)distanceComparator;

@end
