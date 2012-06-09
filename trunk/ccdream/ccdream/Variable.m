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

-(Variable*)init{
    self = [super init];
    if(self!=nil){
        self.changeDate = [NSDate date];
    }
    return self;
}
+(Variable*) copyFromVariable:(Variable*) variable{
    Variable* var = [[[Variable alloc] init] autorelease];
    var.name = variable.name;
    var.value = variable.value;
    return var;
}

- (void)dealloc {
    [_name release];
    _name = nil;
    [_value release];
    _value = nil;
    [_changeDate release];
    _changeDate = nil;
    [super dealloc];
}

@end
