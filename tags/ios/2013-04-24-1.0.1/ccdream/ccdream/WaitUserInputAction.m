//
//  WaitUserInputAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-11.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "WaitUserInputAction.h"
#import "Variable.h"

@implementation WaitUserInputAction

-(State*) onStart:(Session*) session istate:(IState*) previousState event:(Event*) event{
    State* state=[super onStart:session istate:previousState event:event];
    [self playSprite:session istate:previousState event:event];
    return state;
}


-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    CCLOG(@"do nothing at base WaitUserInputAction's play sprite");
}

-(NSString*) onClick:(Position*) mapPosition event:(Event*) event{
    CCLOG(@"click position %@",mapPosition.description);
    return nil;
}

-(void) handle:(Event*) event{
    @try{
        CCLOG(@"receive event[%@]",event);
        [super propagateVariables:event];
        Position* position = [self getVariableValue:PARAM_SELECTED_MAP_POSITION variables:event.variables];
        NSString* result = [self onClick:position event:event];
        if(result!=nil){
            Variable* var = [[[Variable alloc] init] autorelease];
            var.name = PARAM_RESULT;
            var.value = result;
            [event.variables addObject:var];
            [self onEnd:event.state event:event];
        }
    }@catch (NSException* e) {
        CCLOGERROR(@"Exception occured:%@",e);
        @throw e;
    }
}
@end
