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
#import "AITarget.h"

@implementation MoveStrategeAction

-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    MapLayer* mapLayer = [MapLayer sharedMapLayer];
    Character* character = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_CHARACTER nested:YES];
    
    Position* charPosi = character.sourcePosition; 
    CCLOG(@"char posi=%@",charPosi.description);
    Mobility* moblitity = [Mobility mobilityWithDefault];
    
    CCArray* moveOrbitArray = nil;
    //计算可移动范围
    
    moveOrbitArray = [PositionUtil calcMoveOrbitarrayFromPosition:charPosi movement:character.movement mobility:moblitity mapGridAttributeMap:mapLayer.mapGridAttributeMap maxPosition:mapLayer.maxPosi playerCharacterArray:mapLayer.enemyCharacterArray enemyCharacterArray:mapLayer.playerCharacterArray comparator:[MoveOrbit distanceComparator]];

    CCLOG(@"count=%i",[moveOrbitArray count]);
    
    //AI计算最终的移动点
    
    MOVE_STRATEGE moveStratege=character.moveStratege;
    
    //POSITIVE = 1,积极攻击--尽量选找可攻击敌人如果范围内没有敌人则向最近的敌人移动
    //IN_RANGE = 2,范围内攻击--如果1次移动后有可攻击敌人则向敌人移动并且攻击
    //ADHERE = 3,固守反击--不移动只攻击范围内敌人
    //DEFENSIVE_BACK = 4防守反击--如果受到攻击则采用范围内攻击否则采用固守反击
    AITarget* aiTarget = nil;
    if(moveStratege==POSITIVE){
        for (MoveOrbit* mo in moveOrbitArray) {
           CCArray* canAttackTargetArray  = [PositionUtil canAttackTargetArray:mo.position targetCharacterArray:mapLayer.playerCharacterArray rangeSet:character.attackRange];
            if(canAttackTargetArray!=nil&&[canAttackTargetArray count]>0){
                Character* targetCharacter = [canAttackTargetArray objectAtIndex:0];
                //假设敌人只有一把武器 否则需要优化这里的算法
                aiTarget = [AITarget targetCharacter:targetCharacter usingWeapon:[targetCharacter.weapons objectAtIndex:0] moveOrbit:mo];
                break;
            }
        }
        //在没有可攻击的目标的情况下尽量向目标移动
        if(aiTarget==nil){
           
        }
    }else if(moveStratege==IN_RANGE){
        
    }else if(moveStratege==ADHERE){
        
    }else if(moveStratege==DEFENSIVE_BACK){
        
    }
}

@end
