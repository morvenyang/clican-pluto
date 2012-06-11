//
//  NoUserInputAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-11.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "NoUserInputAction.h"


@implementation NoUserInputAction


-(State*) onStart:(Session*) session istate:(IState*) previousState event:(Event*) event{
    State* state=[super onStart:session istate:previousState event:event];
    [self playSprite:session istate:previousState event:event];
    //no user input, we just goes to next state
    [self onEnd:state event:event];
    return state;
}


-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    CCLOG(@"do nothing at base NoUserInputAction's play sprite");
}
@end
