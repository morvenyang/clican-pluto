//
//  MoveAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-12.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "MoveAction.h"
#import "MapLayer.h"
#import "Character.h"

@implementation MoveAction


-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    MapLayer* mapLayer = [self getVariableValueForEvent:event variableName:PARAM_MAP_LAYER nested:YES];
    Character* character = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_CHARACTER nested:YES];
    Position * posi = [self getVariableValue:PARAM_SELECTED_MAP_POSITION variables:event.variables];
    [mapLayer.movementArray removeAllObjects];
    [self cleanShadowSpriteArray:mapLayer.shadowSpriteArray];
    CCLOG(@"move to%@",posi.description);
    
    [character.characterSprite runAction: [CCMoveTo actionWithDuration:0.5 position:[posi toCenterCGPoint]]];
    [self cleanShadowSpriteArray:mapLayer.shadowSpriteArray];
}

-(void) cleanShadowSpriteArray:(CCArray*) shadowSpriteArray{
    int count = [shadowSpriteArray count];
    for(int i=0;i<count;i++){
        CCSprite* shadowSprit = [shadowSpriteArray objectAtIndex:i];
        [shadowSprit removeFromParentAndCleanup:YES];
    }
}
@end
