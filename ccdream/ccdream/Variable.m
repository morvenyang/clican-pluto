//
//  Variable.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Variable.h"


@implementation Variable


@synthesize variableId = _variableId;
@synthesize name = _name;
@synthesize value = _value;
@synthesize changeDate = _changeDate;
@synthesize task = _task;
@synthesize session = _session;
@synthesize event = _event;
@synthesize state = _state;

- (void)dealloc {
    [_name release];
    _name = nil;
    [_value release];
    _value = nil;
    [_changeDate release];
    _changeDate = nil;
    [_task release];
    _task = nil;
    [_session release];
    _session = nil;
    [_event release];
    _event = nil;
    [_state release];
    _state = nil;
    [super dealloc];
}

@end
