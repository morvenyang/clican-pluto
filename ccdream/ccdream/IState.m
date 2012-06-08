//
//  IState.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "IState.h"


@implementation IState

@synthesize name = _name;
@synthesize params = _params;
@synthesize timeoutListeners = _timeoutListeners;
@synthesize stateListeners = _stateListeners;
@synthesize nextStats = _nextStats;
@synthesize nextCondStates = _nextCondStates;
@synthesize propagation = _propagation;
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
    
}

-(void) onStart:(Session*) session istate:(IState*) previousState event:(Event*) event{
    State* state = [[[State alloc] init] autorelease];
    state.value = self.value;
    state.session = session;
    state.name = self.name;
    state.startTime = [NSDate date];
    [session.states addObject:state];
    
    for (id<StateListener> stateListener in self.stateListeners) {
        [stateListener onStart:state previousState:previousState event:event];
    }
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
    
}

-(NSString*) getExprResult:(Event*) event state:(State*) state{
    NSString* result = nil;
    if(event.state!=nil){
        if(event.state.session!=nil){
            for (Variable* var in event.state.session.variables) {
                if([var.name isEqualToString:@"result"]){
                    result = var.value;
                }
            }
        }
        for (Variable* var in event.state.variables) {
            if([var.name isEqualToString:@"result"]){
                result = var.value;
            }
        }
    }
    
    if(state!=nil){
        for (Variable* var in state.variables) {
            if([var.name isEqualToString:@"result"]){
                result = var.value;
            }
        }
    }
    
    if(event!=nil){
        for (Variable* var in event.variables) {
            if([var.name isEqualToString:@"result"]){
                result = var.value;
            }
        }
    }
    
    return result;
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
    [_propagation release];
    _propagation = nil;
    [super dealloc];
}
@end
