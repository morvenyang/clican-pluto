//
//  State.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "State.h"


@implementation State


@synthesize stateId = _stateId;
@synthesize name = _name;
@synthesize value = _value;
@synthesize status = _status;
@synthesize session = _session;
@synthesize jobs = _jobs;
@synthesize tasks = _tasks;
@synthesize events = _events;
@synthesize variables = _variables;
@synthesize startTime = _startTime;
@synthesize endTime = _endTime;

-(id) init {
    self = [super init];
    if(self!=nil){
        self.jobs = [[[NSMutableArray alloc] init] autorelease];
        self.variables = [[[NSMutableArray alloc] init] autorelease];
        self.events = [[[NSMutableArray alloc] init] autorelease];
        self.tasks = [[[NSMutableArray alloc] init] autorelease];
    }
    return self;
}

- (void)dealloc {
    [_name release];
    _name = nil;
    [_status release];
    _status = nil;
    [_jobs release];
    _jobs = nil;
    [_tasks release];
    _tasks = nil;
    [_events release];
    _events = nil;
    [_variables release];
    _variables = nil;
    [_startTime release];
    _startTime = nil;
    [_endTime release];
    _endTime = nil;
    [super dealloc];
}
@end
