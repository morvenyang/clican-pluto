//
//  KpiButton.h
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Kpi.h"
@interface KpiButton : UIButton{
    Kpi* _kpi;
}
@property (nonatomic, retain) Kpi* kpi;
@end
