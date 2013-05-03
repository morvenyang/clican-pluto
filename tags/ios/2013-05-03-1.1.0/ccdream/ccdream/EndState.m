//
//  EndState.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "EndState.h"
#import "Event.h"
#import "Session.h"
#import "State.h"
#import "IState.h"
#import "EngineContext.h"
#import "WorkflowConstants.h"
@implementation EndState


-(State*) onStart:(Session*) session istate:(IState*) previousState event:(Event*) event{
    State* state = [super onStart:session istate:previousState event:event];
    if(![state.status isEqualToString:STATUS_ACTIVE]){
        return state;
    }
    [self onEnd:state event:event];
    return state;
}

-(void) onEnd:(State*) state event:(Event*) event{
    [super onEnd:state event:event];
    Session* session = state.session;
    session.status = STATUS_INACTIVE;
    session.endTime = [NSDate date];
    [[EngineContext sharedEngineContext] deleteSession:session.sessionId];
}


@end
