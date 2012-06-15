//
//  DisplayMoveShadowAndPreMoveAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-11.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "DisplayMoveShadowAndPreMoveAction.h"
#import "Character.h"
#import "MapLayer.h"
#import "Variable.h"
#import "PositionUtil.h"

@implementation DisplayMoveShadowAndPreMoveAction

-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    MapLayer* mapLayer = [MapLayer sharedMapLayer];
    Character* character = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_CHARACTER nested:YES];
    
    Position* charPosi = character.sourcePosition; 
    CCLOG(@"char posi=%@",charPosi.description);
    Mobility* moblitity = [Mobility mobilityWithDefault];
    
    CCArray* posiArray = nil;
    //计算可移动范围
    
    posiArray = [PositionUtil calcMoveOrbitarrayFromPosition:charPosi movement:character.movement mobility:moblitity mapGridAttributeMap:mapLayer.mapGridAttributeMap maxPosition:mapLayer.maxPosi playerCharacterArray:mapLayer.playerCharacterArray enemyCharacterArray:mapLayer.enemyCharacterArray comparator:[MoveOrbit posiComparator]];
    CCLOG(@"count=%i",[posiArray count]);
    
    [self cleanShadowSpriteArray:mapLayer.shadowSpriteArray];
    
    for(int i=0;i<[posiArray count];i++){
        MoveOrbit* mo = [posiArray objectAtIndex:i];
        CCLOG(@"%@",[mo.position description]);
        CCSprite* redSprite = [[CCSprite alloc] init];
        [redSprite setColor:ccc3(255, 0, 0)];
        redSprite.opacity=100;
        redSprite.textureRect = CGRectMake(0, 0, MAP_POINT_SIZE, MAP_POINT_SIZE);
        
        redSprite.position = CGPointMake(mo.position.x*MAP_POINT_SIZE+MAP_POINT_SIZE/2, mo.position.y*MAP_POINT_SIZE+MAP_POINT_SIZE/2);
        [mapLayer.shadowSpriteArray addObject:redSprite];
        [mapLayer addChild:redSprite];
    }
    mapLayer.movementArray = posiArray;
}

-(NSString*) onClick:(Position*) mapPosition event:(Event*) event
{
    [super onClick:mapPosition event:event];
    
    MapLayer* mapLayer = [MapLayer sharedMapLayer];
    Character* character = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_CHARACTER nested:YES];
    [self cleanShadowSpriteArray:mapLayer.shadowSpriteArray];
    NSString* result = nil;
    if(mapLayer.movementArray!=nil&&[PositionUtil containsPosition:mapPosition forMoveOrbitArray:mapLayer.movementArray]){
        result = @"selectMovePoint";
    }else{
        CGPoint cp = [mapPosition toCenterCGPoint];
        if(cp.x==character.characterSprite.position.x&&cp.y==character.characterSprite.position.y){
            //再点击角色
            result = @"selectRoleAgain";
        }else{
            result = @"selectNotMovePoint";
        }
        
    }
    
    return result;
    
}

-(void) cleanShadowSpriteArray:(CCArray*) shadowSpriteArray{
    int count = [shadowSpriteArray count];
    for(int i=0;i<count;i++){
        CCSprite* shadowSprit = [shadowSpriteArray objectAtIndex:i];
        [shadowSprit removeFromParentAndCleanup:YES];
    }
}
@end
