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
    character.sourcePosition = character.targetPosition;
    character.targetPosition = nil;
    [session.actions addObject:[CCCallFuncND actionWithTarget:self selector:@selector(onCallFuncND:data:) data:(void*)character]];
    [character.characterSprite runAction:[CCSequence actionsWithArray:session.actions]];
    [session.actions removeAllObjects];
}
-(void) onCallFuncND:(id)sender data:(void*)data {
    // 如下转换指针的方式要求data必须是一个CCSprite
    Character* character = (Character*)data;
    character.finished = YES;
}

@end
