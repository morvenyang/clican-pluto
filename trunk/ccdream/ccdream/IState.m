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
