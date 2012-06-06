//
//  Job.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Job.h"


@implementation Job


@synthesize jobId = _jobId;
@synthesize name = _name;
@synthesize executeTime = _executeTime;
@synthesize repeatTime = _repeatTime;
@synthesize repeatedTime = _repeatedTime;
@synthesize repeatDuration = _repeatDuration;
@synthesize state = _state;
@synthesize status = _status;

- (void)dealloc {
    [_name release];
    _name = nil;
    [_executeTime release];
    _executeTime = nil;
    [_repeatDuration release];
    _repeatDuration = nil;
    [_state release];
    _state = nil;
    [_status release];
    _status = nil;
    [super dealloc];
}
@end
