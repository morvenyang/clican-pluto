//
//  CalculateMoveAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-16.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "MoveStrategeAction.h"
#import "Character.h"
#import "MapLayer.h"
#import "Variable.h"
#import "PositionUtil.h"

@implementation MoveStrategeAction

-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    MapLayer* mapLayer = [MapLayer sharedMapLayer];
    Character* character = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_CHARACTER nested:YES];
    
    Position* charPosi = character.sourcePosition; 
    CCLOG(@"char posi=%@",charPosi.description);
    Mobility* moblitity = [Mobility mobilityWithDefault];
    
    CCArray* posiArray = nil;
    //计算可移动范围
    
    posiArray = [PositionUtil calcMoveOrbitarrayFromPosition:charPosi movement:3 mobility:moblitity mapGridAttributeMap:mapLayer.mapGridAttributeMap maxPosition:mapLayer.maxPosi playerCharacterArray:mapLayer.enemyCharacterArray enemyCharacterArray:mapLayer.playerCharacterArray];
    CCLOG(@"count=%i",[posiArray count]);
    
    //AI计算最终的移动点
}

@end
