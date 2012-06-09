//
//  TaskState.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "TaskState.h"
#import "Listener.h"
#import "Event.h"
#import "Session.h"
#import "State.h"
#import "Variable.h"
#import "WorkflowConstants.h"
#import "EngineContext.h"

@implementation TaskState

@synthesize taskListeners = _taskListeners;
@synthesize taskName = _taskName;
@synthesize assignees = _assignees;
@synthesize taskType= _taskType;

-(id) init {
    self = [super init];
    if(self){
        self.taskListeners = [[[NSMutableArray alloc] init] autorelease];
    }
    return self;
}

-(State*) onStart:(Session*) session istate:(IState*) previousState event:(Event*) event{
    State* state = [super onStart:session istate:previousState event:event];
    if (![state.status isEqualToString:STATUS_ACTIVE]) {
        return state;
    }
    return state;
}

-(NSMutableArray*) assignTasks:(State*) state event:(Event*) event{
    NSMutableArray* tasks = [[[NSMutableArray alloc] init] autorelease];
    NSArray* assigneeArray = [self.assignees componentsSeparatedByString:@","];
    for (NSString* assignee in assigneeArray) {
        Task* task = [[[Task alloc] init] autorelease];
        task.taskId = [[EngineContext sharedEngineContext] getAndAddAtomicLong];
        task.assignee = assignee;
        task.assignTime =[NSDate date];
        task.name = self.taskName;
        task.type = self.taskType;
        task.state = state;
        task.endTime = [NSDate date];
        for (id<TaskListener> taskListener in self.timeoutListeners) {
            [taskListener beforeAssignTask:task];
        }
        [state.tasks addObject:task];
        [tasks addObject:task];
        for (id<TaskListener> taskListener in self.timeoutListeners) {
            [taskListener afterAssignTask:task];
        }
    }
    return tasks;
}

- (void)dealloc {
    [_taskListeners release];
    _taskListeners = nil;
    [_taskName release];
    _taskName = nil;
    [_assignees release];
    _assignees = nil;
    [_taskType release];
    _taskType = nil;
    [super dealloc];
}

@end
