//
//  Event.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Event.h"


@implementation Event


@synthesize eventId = _eventId;
@synthesize variables = _variables;
@synthesize state = _state;
@synthesize completeTime = _completeTime;
@synthesize eventType = _eventType;

-(id) init{
    self =[super init];
    if(self){
        self.variables = [[[NSMutableArray alloc] init] autorelease];
    }
    return self;
}
- (void)dealloc {
    [_variables release];
    _variables = nil;
    [_completeTime release];
    _completeTime = nil;
    [_eventType release];
    _eventType = nil;
    [super dealloc];
}

@end
