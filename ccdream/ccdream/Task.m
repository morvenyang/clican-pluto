//
//  Task.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Task.h"


@implementation Task


@synthesize taskId = _taskId;
@synthesize name = _name;
@synthesize type = _type;
@synthesize assignee = _assignee;
@synthesize variables = _variables;
@synthesize state = _state;
@synthesize assignTime = _assignTime;
@synthesize completeTime = _completeTime;
@synthesize endTime = _endTime;

-(id) init {
    self = [super init];
    if(self!=nil){
        self.variables = [[[NSMutableArray alloc] init] autorelease];
    }
    return self;
}


- (void)dealloc {
    [_name release];
    _name = nil;
    [_type release];
    _type = nil;
    [_assignee release];
    _assignee = nil;
    [_variables release];
    _variables = nil;
    [_assignTime release];
    _assignTime = nil;
    [_completeTime release];
    _completeTime = nil;
    [_endTime release];
    _endTime = nil;
    [super dealloc];
}

@end
