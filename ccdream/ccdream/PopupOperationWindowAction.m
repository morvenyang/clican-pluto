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
    
    MapLayer* mapLayer = [MapLayer sharedMapLayer];
    Character* character = [self getVariableValueForEvent:event variableName:PARAM_SELECTED_CHARACTER nested:YES];
    [[FightMenuLayer sharedFightMenuLayer] showAtPosition:[character.targetPosition  toFightMenuPosition:mapLayer.maxPosi] charPosi:character.targetPosition character:character];
}

-(NSString*) onClick:(Position*) mapPosition event:(Event*) event
{
    [super onClick:mapPosition event:event];
    NSString* eventType = event.eventType;
    if([eventType isEqualToString:EVENT_TYPE_FM_ATTACK_ONCLICK]){
        return @"selectAttack";
    }else if([eventType isEqualToString:EVENT_TYPE_FM_STANDBY_ONCLICK]){
        return @"selectStandby";
    }else if([eventType isEqualToString:EVENT_TYPE_FM_ITEM_ONCLICK]){
        return @"selectItem";
    }else if([eventType isEqualToString:EVENT_TYPE_FM_MOUNTHOUSE_ONCLICK]){
        return @"selectMountHorse";
    }else if([eventType isEqualToString:EVENT_TYPE_FM_DISMOUNTHOUSE_ONCLICK]){
        return @"selectDismountHorse";
    }else if([eventType isEqualToString:EVENT_TYPE_FM_CANCEL_ONCLICK]){
        return @"selectCancel";
    }else{
        CCLOGERROR(@"Not supported event type %@",eventType);
        return nil;
    }
    
}
@end
