//
//  IState.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "IState.h"
#import "Listener.h"
#import "Event.h"
#import "Session.h"
#import "State.h"
#import "Variable.h"
#import "EngineContext.h"


@implementation IState

@synthesize name = _name;
@synthesize params = _params;
@synthesize timeoutListeners = _timeoutListeners;
@synthesize stateListeners = _stateListeners;
@synthesize nextStats = _nextStats;
@synthesize nextCondStates = _nextCondStates;
@synthesize value = _value;

- (id) init{
    self = [super init];
    if(self){
        self.stateListeners = [[[NSMutableArray alloc] init] autorelease];
        self.nextStats = [[[NSMutableArray alloc] init] autorelease];
        self.nextCondStates = [[[NSMutableDictionary alloc] init] autorelease];
    }
    return self;
}

-(void) handle:(Event*) event{
    @try{
        CCLOG(@"receive event[%@]",event);
        [self propagateVariables:event];
        [self onEnd:event.state event:event];
    }@catch (NSException* e) {
        CCLOGERROR(@"Exception occured:%@",e);
        @throw e;
    }
}

-(void) propagateVariables:(Event*) event{
    State* state = event.state;
    Session* session = event.state.session;
    for (Variable* var in event.variables) {
        Variable* v1 = [Variable copyFromVariable:var];
        Variable* v2 = [Variable copyFromVariable:var];
        v1.state = state;
        v2.session = session;
        [state.variables addObject:v1];
        [session.variables addObject:v2];
    }
    session.lastUpdateTime = [NSDate date];
}
-(State*) onStart:(Session*) session istate:(IState*) previousState event:(Event*) event{
    State* state = [[[State alloc] init] autorelease];
    state.stateId = [[EngineContext sharedEngineContext] getAndAddAtomicLong];
    state.value = self.value;
    state.session = session;
    state.name = self.name;
    state.startTime = [NSDate date];
    state.status = STATUS_ACTIVE;
    [session.states addObject:state];
    
    for (id<StateListener> stateListener in self.stateListeners) {
        [stateListener onStart:state previousState:previousState event:event];
    }
    return state;
}

-(void) onEnd:(State*) state event:(Event*) event{
    NSArray* nextStateArray = nil;
    
    [[[NSMutableArray alloc] init] autorelease];
    if(self.nextStats!=nil&&[self.nextStats count]!=0){
        nextStateArray = self.nextStats;
    }else{
        NSString* result =[self getExprResult:event state:state];
        if(self.nextCondStates!=nil&&[self.nextCondStates count]!=0){
            for (NSString* expr in [self.nextCondStates allKeys]) {
                if([expr isEqualToString:result]){
                    nextStateArray = [self.nextCondStates objectForKey:expr];
                    break;
                }
            }
        }
        if(nextStateArray==nil){
            CCLOGERROR(@"Can't find expected result");
        }
    }
    for (id<StateListener> stateListener in self.stateListeners) {
        [stateListener onEnd:state nextStateList:nextStateArray event:event];
    }
    
    state.status = STATUS_INACTIVE;
    state.endTime = [NSDate date];
    state.session.lastUpdateTime = [NSDate date];
    for (IState* nextState in nextStateArray) {
        [nextState onStart:state.session istate:self event:event];
    }
}

-(NSString*) getExprResult:(Event*) event state:(State*) state{
    NSString* result = nil;
    if(event.state!=nil){
        if(event.state.session!=nil){
            for (Variable* var in event.state.session.variables) {
                if([var.name isEqualToString:PARAM_RESULT]){
                    result = var.value;
                }
            }
        }
        for (Variable* var in event.state.variables) {
            if([var.name isEqualToString:PARAM_RESULT]){
                result = var.value;
            }
        }
    }
    
    if(state!=nil){
        for (Variable* var in state.variables) {
            if([var.name isEqualToString:PARAM_RESULT]){
                result = var.value;
            }
        }
    }
    
    if(event!=nil){
        for (Variable* var in event.variables) {
            if([var.name isEqualToString:PARAM_RESULT]){
                result = var.value;
            }
        }
    }
    
    return result;
}

-(id) getVariableValue:(NSString*) variableName variables:(NSArray*) variables{
    if(variableName==nil||variables==nil){
        return nil;
    }
    for (Variable* var in variables) {
        if([var.name isEqualToString:variableName]){
            return var.value;
        }
    }
    return nil;
}

-(id) getVariableValueForTask:(Task*) task variableName:(NSString*) variableName nested:(BOOL) nested{
    NSString* value = [self getVariableValue:variableName variables:task.variables];
    if(value!=nil){
        return value;
    }  
    if(nested){
        value = [self getVariableValueForState:task.state variableName:variableName nested :nested];
    }
    return value;
}

-(id) getVariableValueForEvent:(Event*) event variableName:(NSString*) variableName nested:(BOOL) nested{
    NSString* value = [self getVariableValue:variableName variables:event.variables];
    if(value!=nil){
        return value;
    }  
    if(nested){
        value = [self getVariableValueForState:event.state variableName:variableName nested :nested];
    }
    return value;
}

-(id) getVariableValueForState:(State*) state variableName:(NSString*) variableName nested:(BOOL) nested{
    NSString* value = [self getVariableValue:variableName variables:state.variables];
    if(value!=nil){
        return value;
    }  
    if(nested){
        value = [self getVariableValueForSession:state.session variableName:variableName];
    }
    return value;
}

-(NSString*) getVariableValueForSession:(Session*) session variableName:(NSString*) variableName{
    NSString* value = [self getVariableValue:variableName variables:session.variables];
    return value;
}
- (void)dealloc {
    [_name release];
    _name = nil;
    [_params release];
    _params = nil;
    [_timeoutListeners release];
    _timeoutListeners = nil;
    [_stateListeners release];
    _stateListeners = nil;
    [_nextStats release];
    _nextStats = nil;
    [_nextCondStates release];
    _nextCondStates = nil;
    [super dealloc];
}
@end
