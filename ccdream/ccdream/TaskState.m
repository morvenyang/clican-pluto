//
//  TaskState.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "TaskState.h"


@implementation TaskState

@synthesize taskListeners = _taskListeners;

-(id) init {
    self = [super init];
    if(self){
        self.taskListeners = [[[NSMutableArray alloc] init] autorelease];
    }
    return self;
}

- (void)dealloc {
    [_taskListeners release];
    _taskListeners = nil;
    [super dealloc];
}

@end
