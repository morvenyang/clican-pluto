//
//  IState.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Event.h"
#import "Session.h"
#import "State.h"

@interface IState : NSObject {
    NSString* _name;
    NSDictionary* _params;
    NSArray* _stateListeners;
    NSDictionary* _timeoutListeners;
    NSDictionary* _nextCondStates;
    NSArray* _nextStats;
    NSString* _propagation;
    int _value;
}

@property (nonatomic,retain) NSString* name;
@property (nonatomic,retain) NSDictionary* params;
@property (nonatomic,retain) NSArray* stateListeners;
@property (nonatomic,retain) NSDictionary* timeoutListeners;
@property (nonatomic,retain) NSDictionary* nextCondStates;
@property (nonatomic,retain) NSArray* nextStats;
@property (nonatomic,retain) NSString* propagation;
@property (nonatomic,assign) int value;

//-(void) handle:(Event*) event;

//-(void) onStart:(Session*) session istate:(IState*) previousState event:(Event*) event;

//-(void) onEnd:(State*) state event:(Event*) event;

@end
