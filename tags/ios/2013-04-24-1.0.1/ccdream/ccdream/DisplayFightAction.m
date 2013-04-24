//
//  DisplayFightAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-13.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "DisplayFightAction.h"
#import "Character.h"
#import "MapLayer.h"
#import "Variable.h"
@implementation DisplayFightAction

-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    
    Character* target = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_TARGET nested:NO];
   
    [target.characterSprite removeFromParentAndCleanup:YES];
    if(target.player){
        [[MapLayer sharedMapLayer].playerCharacterArray removeObject:target];
    }else{
        [[MapLayer sharedMapLayer].enemyCharacterArray removeObject:target];
    }
    Variable* var = [Variable variable:PARAM_RESULT value:@"cannotReMove"];
    [event.variables addObject:var];
}

@end
