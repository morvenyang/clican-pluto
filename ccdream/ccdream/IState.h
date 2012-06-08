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
#import "Variable.h"
#import "Listener.h"



@interface IState : NSObject {
    NSString* _name;
    NSMutableDictionary* _params;
    NSMutableArray* _stateListeners;
    NSMutableDictionary* _timeoutListeners;
    NSMutableDictionary* _nextCondStates;
    NSMutableArray* _nextStats;
    NSString* _propagation;
    int _value;
}

@property (nonatomic,retain) NSString* name;
@property (nonatomic,retain) NSMutableDictionary* params;
@property (nonatomic,retain) NSMutableArray* stateListeners;
@property (nonatomic,retain) NSMutableDictionary* timeoutListeners;
@property (nonatomic,retain) NSMutableDictionary* nextCondStates;
@property (nonatomic,retain) NSMutableArray* nextStats;
@property (nonatomic,retain) NSString* propagation;
@property (nonatomic,assign) int value;

-(void) handle:(Event*) event;

-(void) onStart:(Session*) session istate:(IState*) previousState event:(Event*) event;

-(void) onEnd:(State*) state event:(Event*) event;

@end
