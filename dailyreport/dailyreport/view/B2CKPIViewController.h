//
//  B2CKPIViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-7-3.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"
#import "KPIModel.h"
@interface B2CKPIViewController : SwitchViewController<KPIDelegate>{
    KPIModel* _kpiModel;
}
@property (nonatomic, retain) KPIModel *kpiModel;
-(id) initWithBrand:(NSString*) brand;
@end
