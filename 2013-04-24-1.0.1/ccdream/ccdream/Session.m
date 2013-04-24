//
//  Session.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Session.h"


@implementation Session

@synthesize sessionId = _sessionId;
@synthesize title = _title;
@synthesize name = _name;
@synthesize type = _type;
@synthesize sponsor = _sponsor;
@synthesize startTime = _startTime;
@synthesize lastUpdateTime = _lastUpdateTime;
@synthesize endTime = _endTime;
@synthesize states = _states;
@synthesize variables = _variables;
@synthesize status = _status;
@synthesize actions = actions;
-(id) init {
    self = [super init];
    if(self!=nil){
        self.startTime = [NSDate date];
        self.lastUpdateTime = [NSDate date];
        self.states = [[[NSMutableArray alloc] init] autorelease];
        self.variables = [[[NSMutableArray alloc] init] autorelease];
        self.status = @"active";
        self.actions = [[[NSMutableArray alloc] init] autorelease];
    }
    return self;
}
- (void)dealloc {
    [_name release];
    _name = nil;
    [_title release];
    _title = nil;
    [_type release];
    _type = nil;
    [_sponsor release];
    _sponsor = nil;
    [_startTime release];
    _startTime = nil;
    [_lastUpdateTime release];
    _lastUpdateTime = nil;
    [_endTime release];
    _endTime = nil;
    [_states release];
    _states = nil;
    [_variables release];
    _variables = nil;
    [_status release];
    _status = nil;
    [_actions release];
    _actions = nil;
    [super dealloc];
}

@end
