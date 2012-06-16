//
//  Character.h
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//
//  这个类是用来描述游戏人物的模型
#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Mobility.h"


#import "GlobalEventHandler.h"

typedef enum {
    POSITIVE = 1,//极攻击--尽量选找可攻击敌人如果范围内没有敌人则向最近的敌人移动
    IN_RANGE = 2,//范围内攻击--如果1次移动后有可攻击敌人则向敌人移动并且攻击
    ADHERE = 3//固守反击--不移动只攻击范围内敌人
    
} MOVE_STRATEGE;

@interface Character : NSObject {
    Mobility* _landMobility;
    Mobility* _airMobility;
    int _movement;
    CCSprite* _characterSprite;
    UIImage* _sourceCharacterImage;
    UIImage* _grayCharacterImage;
    Position* _sourcePosition;
    Position* _targetPosition;
    CCArray* _weapons;
    NSMutableSet* _attackRange;
    BOOL _canMountHorse;
    BOOL _mountHorse;
    BOOL _selected;
    BOOL _finished;
    BOOL _player;//YES:代表是玩家 NO:代表是敌人
    
    MOVE_STRATEGE _moveStratege;
    
    
}

@property (nonatomic,retain) Mobility* landMobility;
@property (nonatomic,retain) Mobility* airMobility;
@property (nonatomic,assign) int movement;
@property (nonatomic,retain) CCSprite* characterSprite;
@property (nonatomic,retain) UIImage* sourceCharacterImage;
@property (nonatomic,retain) UIImage* grayCharacterImage;
@property (nonatomic,retain) Position* sourcePosition;
@property (nonatomic,retain) Position* targetPosition;
@property (nonatomic,retain) CCArray* weapons;
@property (nonatomic,retain) NSMutableSet* attackRange;
@property (nonatomic,assign)  BOOL canMountHorse;
@property (nonatomic,assign)  BOOL mountHorse;
@property (nonatomic,assign)  BOOL selected;
@property (nonatomic,assign)  BOOL finished;
@property (nonatomic,assign)  BOOL player;
@property (nonatomic,assign)  MOVE_STRATEGE moveStratege;
+(id)characterWithParentNode:(CCNode*) parentNode spriteFile:(NSString*) spriteFile position:(Position*) position;

@end


