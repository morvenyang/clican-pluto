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

@interface FightMenuLayer : CCLayer<PositionTouchDelegate> {
    CCMenu* _fightMenu;
    CCMenuItemFont* _attack;
    CCMenuItemFont* _standby;
    CCMenuItemFont* _cancel;
    CCMenuItemFont* _mountHorse;
    CCMenuItemFont* _dismountHorse;
    CCMenuItemFont* _item;


}

@property (nonatomic,retain) CCMenu* fightMenu;
@property (nonatomic,retain) CCMenuItemFont* attack;
@property (nonatomic,retain) CCMenuItemFont* standby;
@property (nonatomic,retain) CCMenuItemFont* cancel;
@property (nonatomic,retain) CCMenuItemFont* mountHorse;
@property (nonatomic,retain) CCMenuItemFont* dismountHorse;
@property (nonatomic,retain) CCMenuItemFont* item;


+(FightMenuLayer*) sharedFightMenuLayer;

-(void) showAtPosition:(Position*) position charPosi:(Position*) charPosi character:(Character*) character;
-(void) hide;

@end
