//
//  Weapon.h
//  ccdream
//
//  Created by wei zhang on 12-6-10.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"

@interface Weapon : NSObject {
    //基础属性
    NSString* _name;//武器名称
    UIImage* _image;//武器图片
    NSString* _imageFile;//武器图片
    CCSprite* _weaponSprite;
    int _minRange;//最小攻击范围
    int _maxRange;//最大攻击范围
    NSMutableSet* _rangeSet;
    int _phyPower;//物理攻击力
    int _magPower;//魔法攻击力
    int _requiredLevel;//武器使用级别
    int _weight;//武器重量
    int _hit;//武器命中加成
    int _critical;//武器必杀加成
    BOOL _armorAgainst;//对重甲兵特效
    BOOL _cavalryAgainst;//对骑兵特效
    BOOL _flyAgainst;//对飞兵特效
    BOOL _monsterAgainst;//对怪兽特效

}

@property (nonatomic,retain) NSString* name;
@property (nonatomic,retain) UIImage* image;
@property (nonatomic,retain) NSString* imageFile;
@property (nonatomic,retain) CCSprite* weaponSprite;
@property (nonatomic,assign) int minRange;
@property (nonatomic,assign) int maxRange;
@property (nonatomic,retain) NSMutableSet* rangeSet;
@property (nonatomic,assign) int phyPower;
@property (nonatomic,assign) int magPower;
@property (nonatomic,assign) int requiredLevel;
@property (nonatomic,assign) int weight;
@property (nonatomic,assign) int hit;
@property (nonatomic,assign) int critical;
@property (nonatomic,assign) BOOL armorAgainst;
@property (nonatomic,assign) BOOL cavalryAgainst;
@property (nonatomic,assign) BOOL flyAgainst;
@property (nonatomic,assign) BOOL monsterAgainst;


+(Weapon*) weaponWithDict:(NSDictionary*) dict;
@end
