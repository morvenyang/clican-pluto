//
//  FightMenuLayer.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//
//  战斗弹出菜单

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "GlobalEventHandler.h"
#import "Character.h"
#import "Position.h"

@interface FightMenuLayer : CCLayer<CharacterSelectDelegate,PositionTouchDelegate> {
    CCMenu* _fightMenu;
    CCMenuItemFont* _attack;
    CCMenuItemFont* _standby;
    CCMenuItemFont* _cancel;
    BOOL _selectAttack;
}

@property (nonatomic,retain) CCMenu* fightMenu;
@property (nonatomic,retain) CCMenuItemFont* attack;
@property (nonatomic,retain) CCMenuItemFont* standby;
@property (nonatomic,retain) CCMenuItemFont* cancel;
@property (nonatomic,assign) BOOL selectAttack;
+(FightMenuLayer*) sharedFightMenuLayer;

-(void) showAtPosition:(Position*) position;
-(void) hide;

@end
