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
}

@property (nonatomic,retain) MoveOrbit* previous;
@property (nonatomic,retain) Position* position;

+(id)moveOrbitWithPrevious:(MoveOrbit*) previous Position:(Position*) position;

+(id)comparator;
@end
