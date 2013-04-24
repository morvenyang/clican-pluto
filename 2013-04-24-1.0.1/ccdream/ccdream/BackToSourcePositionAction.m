//
//  BackToSourcePositionAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-13.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "BackToSourcePositionAction.h"
#import "Character.h"

@implementation BackToSourcePositionAction

-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    Character* character = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_CHARACTER nested:YES];
    [character.characterSprite runAction: [CCMoveTo actionWithDuration:0 position:[character.sourcePosition toCenterCGPoint]]];
}

@end
