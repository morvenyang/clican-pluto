//
//  PopupOperationWindowAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-12.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "PopupOperationWindowAction.h"
#import "MapLayer.h"

@implementation PopupOperationWindowAction

-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    Position* position = [self getVariableValue:PARAM_SELECTED_MAP_POSITION variables:event.variables];
    MapLayer* mapLayer = [self getVariableValueForEvent:event variableName:PARAM_MAP_LAYER nested:YES];
    [[FightMenuLayer sharedFightMenuLayer] showAtPosition:[position  toFightMenuPosition:mapLayer.maxPosi]];
}
@end
