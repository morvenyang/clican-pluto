//
//  PopupWeaponSelectWindowAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-13.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "PopupWeaponSelectWindowAction.h"
#import "WeaponLayer.h"
#import "MapLayer.h"
#import "PositionUtil.h"
#import "Weapon.h"
#import "ImageUtil.h"
#import "Variable.h"

@implementation PopupWeaponSelectWindowAction

-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
     MapLayer* mapLayer=[MapLayer sharedMapLayer];
    WeaponLayer* weaponLayer = [WeaponLayer sharedWeaponLayer];
    
     Position* position = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_MAP_POSITION nested:YES];
    Character* character = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_CHARACTER nested:YES];
    [weaponLayer showAtPosition:[position toWeaponMenuPosition:[MapLayer sharedMapLayer].maxPosi] charPosi:position character:character];
    Weapon* weapon = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_WEAPON nested:NO];
    if(weapon==nil){
        weapon = [weaponLayer.weapons objectAtIndex:0];
    }
    CCLOG(@"select weapon:%@",weapon.name);
    weaponLayer.targetCharacterArray = [PositionUtil canAttackTargetArray:position targetCharacterArray:mapLayer.enemyCharacterArray rangeSet:weapon.rangeSet];
    [weaponLayer.targetShadowSpriteArray removeAllObjects];
    for (Character* character in weaponLayer.targetCharacterArray) {
        CCSprite* redSprite = [[CCSprite alloc] init];
        [redSprite setColor:ccc3(255, 0, 0)];
        redSprite.opacity=100;
        redSprite.textureRect = CGRectMake(0, 0, MAP_POINT_SIZE_X, MAP_POINT_SIZE_Y);
        redSprite.position = character.characterSprite.position;
        [weaponLayer.targetShadowSpriteArray addObject:redSprite];
        [mapLayer addChild:redSprite];
    }
}

-(void) cleanShadowSpriteArray:(CCArray*) shadowSpriteArray{
    int count = [shadowSpriteArray count];
    for(int i=0;i<count;i++){
        CCSprite* shadowSprit = [shadowSpriteArray objectAtIndex:i];
        [shadowSprit removeFromParentAndCleanup:YES];
    }
}

-(NSString*) onClick:(Position*) mapPosition event:(Event*) event{
    CCLOG(@"click position %@",mapPosition.description);
    WeaponLayer* weaponLayer = [WeaponLayer sharedWeaponLayer];
    Weapon* weapon = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_WEAPON nested:NO];
    [self cleanShadowSpriteArray:weaponLayer.targetShadowSpriteArray];
    if(weapon!=nil){
        //更换武器
        [self playSprite:event.state.session istate:nil event:event];
        return nil;
    } else {
        Character* character = [PositionUtil getCharacter:mapPosition forCharacterArray:weaponLayer.targetCharacterArray];
        if(character!=nil){
            //选中目标敌人
            Variable* var = [Variable variable:PARAM_SELECTED_TARGET value:character];
            [event.variables addObject:var];
            return @"selectTarget";
        }else{
            return @"selectCancel";
        }
    }
}

@end
