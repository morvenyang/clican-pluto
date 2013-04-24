//
//  WorkflowConstants.h
//  ccdream
//
//  Created by wei zhang on 12-6-9.
//  Copyright 2012年 Clican. All rights reserved.
//

#define STATUS_ACTIVE @"active"
#define STATUS_INACTIVE @"inactive"
#define EVENT_TYPE_TASK @"task"
#define EVENT_TYPE_JOB @"job"
#define EVENT_TYPE_NORMAL @"normal"

//MapLayer上的event
#define EVENT_TYPE_MAP_CHARACTER_ONCLICK @"character_onclick"
#define EVENT_TYPE_MAP_ONCLICK @"map_onclick"
//FightMenuLayer上的event
#define EVENT_TYPE_FM_STANDBY_ONCLICK @"fm_standby_onclick"
#define EVENT_TYPE_FM_MOUNTHOUSE_ONCLICK @"fm_mounthorse_onclick"
#define EVENT_TYPE_FM_DISMOUNTHOUSE_ONCLICK @"fm_dismounthorse_onclick"
#define EVENT_TYPE_FM_ITEM_ONCLICK @"fm_item_onclick"
#define EVENT_TYPE_FM_ATTACK_ONCLICK @"fm_attack_onclick"
#define EVENT_TYPE_FM_CANCEL_ONCLICK @"fm_cancel_onclick"

//WeaponLayer上的event
#define EVENT_TYPE_WL_SELECT_WEAPON @"wl_select_weapon"

#define PARAM_TASK_ID @"taskId"
#define PARAM_TASK_ASSIGNEE @"assignee"
#define PARAM_RESULT @"result"
#define PARAM_SELECTED_CHARACTER @"selected_character"
#define PARAM_SELECTED_TARGET @"selected_target"
#define PARAM_SELECTED_WEAPON @"selected_weapon"
#define PARAM_SELECTED_MAP_POSITION @"selected_map_position"
#define PARAM_SELECTED_MODEL @"selected_model"
#define PARAM_AI_TARGET @"ai_target"
@interface WorkflowConstants : NSObject {
    
}

@end
