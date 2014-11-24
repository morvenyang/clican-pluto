//
//  NoRetailsController.h
//  dailyreport
//
//  Created by zhang wei on 14-11-24.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"
#import "NoRetailModel.h"
@interface NoRetailsController : SwitchViewController<NoRetailDelegate>{
    NoRetailModel* _noRetailModel;
    CGFloat _tableOffset;
}
@property (nonatomic, retain) NoRetailModel *noRetailModel;
-(id) initWithBrand:(NSString*) brand;
@end
