//
//  EventDispatcher.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "EventDispatcher.h"
#import "Session.h"
#import "State.h"
#import "Event.h"
#import "EngineContext.h"
#import "Variable.h"
#import "IState.h"

@implementation EventDispatcher

static EventDispatcher *sharedEventDispatcher = nil;

+(EventDispatcher*) sharedEventDispatcher{
    @synchronized(self) {
		if (sharedEventDispatcher == nil){
            sharedEventDispatcher = [[self alloc] init]; // assignment not done here
        }
	}
	return sharedEventDispatcher;
}


-(void) dispatch:(long) sessionId forState:(long) stateId forEventType:(NSString*) eventType forParameters:(NSDictionary*) parameters{
    @try {
        EngineContext* engineContext = [EngineContext sharedEngineContext];
        
        Event* event = [[[Event alloc] init] autorelease];
        event.completeTime = [NSDate date];
        State* activeState = nil;
        if(stateId==0){
             activeState = [engineContext findActiveStateBySessionId:sessionId];
        }else{
            activeState = [engineContext findStateById:stateId sessionId:sessionId];
        }
        
        if(activeState==nil||![activeState.status isEqualToString:@"active"]){
            [NSException raise:@"Active state not found" format:@"There is no active state for session[%l] state[%l]",sessionId,stateId];
        }
        event.state = activeState;
        event.eventType = eventType;
        if(parameters!=nil){
            for (NSString* name in parameters.allKeys) {
                id value = [parameters objectForKey:name];
                Variable* var = [[[Variable alloc] init] autorelease];
                var.name = name;
                var.value = value;
                var.event = event;
                var.changeDate = [NSDate date];
                [event.variables addObject:var];
            }
        }
        IState* istate = [engineContext getState:activeState.session.name stateName:activeState.name];
        [istate handle:event];
    }
    @catch (NSException *exception) {
        CCLOGERROR(@"handle event exception%@",exception);
        @throw exception;
    }
    @finally {
        
    }
}
@end
