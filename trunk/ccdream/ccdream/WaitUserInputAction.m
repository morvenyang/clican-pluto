//
//  WaitUserInputAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-11.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "WaitUserInputAction.h"


@implementation WaitUserInputAction

-(State*) onStart:(Session*) session istate:(IState*) previousState event:(Event*) event{
    State* state=[super onStart:session istate:previousState event:event];
    [self playSprite:session istate:previousState event:event];
    return state;
}


-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    CCLOG(@"do nothing at base WaitUserInputAction's play sprite");
}
@end
