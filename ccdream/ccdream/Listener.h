//
//  Listener.h
//  ccdream
//
//  Created by wei zhang on 12-6-7.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Task.h"
#import "State.h"
#import "IState.h"
#import "Event.h"

@protocol TaskListener <NSObject>

@optional

-(BOOL) beforeAssignTask:(Task*) task;

-(void) afterAssignTask:(Task*) task;

-(void) beforeHandleTask:(Task*) task;

-(void) afterHandleTask:(Task*) task;

@end

@protocol StateListener <NSObject>

@optional

-(void) onStart:(State*) state previousState:(IState*) state event:(Event*)event;

-(void) onEnd:(State*) state nextStateList:(NSArray*) nextStateList event:(Event*) event;


@end
