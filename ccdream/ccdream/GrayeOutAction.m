//
//  GrayeOutAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-13.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "GrayeOutAction.h"
#import "Character.h"

@implementation GrayeOutAction



-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    Character* character = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_CHARACTER nested:YES];
    [character.characterSprite.texture initWithImage:character.grayCharacterImage];
    character.sourcePosition = [Position positionWithCGPoint:character.characterSprite.position];
    character.finished = YES;
}


@end
