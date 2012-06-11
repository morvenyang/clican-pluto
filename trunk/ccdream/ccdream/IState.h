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
#import "Task.h"
#import "WorkflowConstants.h"

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
@property (nonatomic,assign) int value;


-(void) handle:(Event*) event;

-(State*) onStart:(Session*) session istate:(IState*) previousState event:(Event*) event;

-(void) onEnd:(State*) state event:(Event*) event;

-(void) propagateVariables:(Event*) event;

-(id) getVariableValue:(NSString*) variableName variables:(NSArray*) variables;

-(NSString*) getVariableValueForTask:(Task*) task variableName:(NSString*) variableName nested:(BOOL) nested;

-(NSString*) getVariableValueForEvent:(Event*) event variableName:(NSString*) variableName nested:(BOOL) nested;

-(NSString*) getVariableValueForState:(State*) state variableName:(NSString*) variableName nested:(BOOL) nested;

-(NSString*) getVariableValueForSession:(Session*) session variableName:(NSString*) variableName;


@end
