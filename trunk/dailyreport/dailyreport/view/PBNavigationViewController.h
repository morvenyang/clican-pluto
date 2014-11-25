//
//  PBNavigationViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-11-25.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"

@interface PBNavigationViewController : SwitchViewController{
    int _backIndex;
}
@property (nonatomic, assign) int backIndex;
-(id) initWithBrand:(NSString*) brand backIndex:(int)backIndex;
@end
